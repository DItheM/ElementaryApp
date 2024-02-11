package com.example.elementaryapp.content

import android.os.Bundle
import android.view.MotionEvent
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.elementaryapp.R
import com.example.elementaryapp.classes.VrModel
import com.example.elementaryapp.recycler_view.RecycleViewAdapterVrModels
import com.example.elementaryapp.services.Services
import com.google.ar.core.Anchor
import com.google.ar.core.HitResult
import com.google.ar.core.Plane
import com.google.ar.sceneform.AnchorNode
import com.google.ar.sceneform.Node
import com.google.ar.sceneform.SceneView
import com.google.ar.sceneform.math.Vector3
import com.google.ar.sceneform.rendering.ModelRenderable
import com.google.ar.sceneform.rendering.Renderable
import com.google.ar.sceneform.rendering.ViewRenderable
import com.google.ar.sceneform.ux.ArFragment
import com.google.ar.sceneform.ux.TransformableNode
import com.gorisse.thomas.sceneform.light.LightEstimationConfig
import com.gorisse.thomas.sceneform.lightEstimationConfig
import com.gorisse.thomas.sceneform.scene.await


class VrSceenActivity : AppCompatActivity() {
    private lateinit var adapter: RecycleViewAdapterVrModels

    private lateinit var recyclerView: RecyclerView

    private lateinit var list: ArrayList<VrModel>
    private lateinit var selectedModel: VrModel

    private lateinit var arFragment: ArFragment
    private val arSceneView get() = arFragment.arSceneView
    private val scene get() = arSceneView.scene

    private var model: Renderable? = null
    private var modelView: ViewRenderable? = null
    private var viewNode: Node? = null
    private var transformableNode: TransformableNode? = null
    private var anchorNode: AnchorNode? = null
    private var isModelPlaced = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vr_screen)

        Services.onPressBack(this)
        recyclerView = findViewById(R.id.recyclerViewModel)

        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = RecyclerView.HORIZONTAL
        recyclerView.layoutManager = layoutManager

        list = java.util.ArrayList<VrModel>()

        list.add(VrModel("None", -1, R.drawable.none))
        list.add(VrModel("Lion", R.raw.lion_lowpoly_1k, R.drawable.lion))
        list.add(VrModel("Cat", R.raw.cat_model, R.drawable.cat))
        list.add(VrModel("Dog", R.raw.animated_dog_sits_rolls_over_shake_paw, R.drawable.dog))
        list.add(VrModel("Bird", R.raw.bird_orange, R.drawable.orange_bird))
        list.add(VrModel("Fish", R.raw.koi_fish, R.drawable.fish))
//        list.add(VrModel("Elephant", R.raw.elephant, R.drawable.elephant))
        list.add(VrModel("Giraffe", R.raw.girafe, R.drawable.girrafe))
        list.add(VrModel("T-Rex Dinosaur", R.raw.trex, R.drawable.trex))

        selectedModel = list[0]

        adapter = RecycleViewAdapterVrModels(this, list, recyclerView)
        recyclerView.adapter = adapter

        adapter.setOnItemClickListener(RecycleViewAdapterVrModels.OnItemClickListener { position ->
            if (selectedModel.name != list[position].name) {
                selectedModel = list[position]
                Toast.makeText(this, "You selected: ${selectedModel.name}", Toast.LENGTH_SHORT)
                    .show()
                onTapItem()
            }
        })

        arFragment = supportFragmentManager.findFragmentById(R.id.arFragment) as ArFragment
        arFragment.setOnViewCreatedListener { arSceneView ->
            arSceneView.setFrameRateFactor(SceneView.FrameRate.FULL)
            arSceneView.lightEstimationConfig = LightEstimationConfig.DISABLED
        }
        arFragment.setOnTapArPlaneListener { hitResult: HitResult, plane: Plane, motionEvent ->
            placeObject(hitResult.createAnchor())
        }
    }

    private fun onTapItem() {
        anchorNode?.let { anchorNodePara ->
            scene.removeChild(anchorNodePara)
            anchorNode = null
            isModelPlaced = false
        }
    }

    private fun placeObject(anchor: Anchor) {
        if (isModelPlaced) {
            Toast.makeText(this, "Only one model can be placed at a time", Toast.LENGTH_SHORT).show()
            return
        }

        val modelResourse = selectedModel.model
        if (modelResourse != -1) {
            Toast.makeText(this, "Loading...", Toast.LENGTH_SHORT).show()
            val modelRenderable = ModelRenderable.builder()
                .setSource(this, modelResourse)
                .setIsFilamentGltf(true)
                .build()

            val viewRenderable = ViewRenderable.builder()
                .setView(this, R.layout.vr_fragment)
                .build()

            // Wait for both model and view renderables to be built
            modelRenderable.thenAccept { model ->
                viewRenderable.thenAccept { view ->
                    anchorNode = AnchorNode(anchor)
                    anchorNode!!.parent = arFragment.arSceneView.scene

                    transformableNode = TransformableNode(arFragment.transformationSystem)
                    transformableNode!!.parent = anchorNode
                    transformableNode!!.renderable = model

                    // Adjust the scale of the model
                    val targetScale = calculateTargetScale(modelResourse) // Implement this function

                    transformableNode!!.scaleController.isEnabled = true
//                    transformableNode!!.localScale = Vector3(0f, 0f, targetScale)
                    transformableNode!!.scaleController.minScale = targetScale - 0.001f
                    transformableNode!!.scaleController.maxScale = targetScale

                    transformableNode!!.renderableInstance.setCulling(false)
                    transformableNode!!.renderableInstance.animate(true).start()


                    // Add the view
                    viewNode = Node().apply {
                        localPosition = Vector3(0.0f, 0.15f, 0.0f) // Adjust position as needed
                        renderable = view
                    }
                    transformableNode!!.addChild(viewNode)
                    isModelPlaced = true
                }
            }.exceptionally {
                Toast.makeText(this, "Error loading model: $it", Toast.LENGTH_SHORT).show()
                null
            }
        } else {
            Toast.makeText(this, "Select model first", Toast.LENGTH_SHORT).show()
        }
    }

    private fun calculateTargetScale(model: Int): Float {
        var value = 0.2f
        if (model == R.raw.lion_lowpoly_1k) {
            value = 0.7f
        } else if (model == R.raw.animated_dog_sits_rolls_over_shake_paw){
            value = 0.2f
        } else if (model == R.raw.cat_model) {
            value = 0.015f
        } else if (model == R.raw.bird_orange) {
            value = 0.1f
        } else if (model == R.raw.trex) {
            value = 0.9f
        }
        return value
    }
}