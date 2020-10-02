package my.edu.tarc.finalyearproject;

import android.content.Context;

import com.google.ar.core.AugmentedImage;
import com.google.ar.core.Pose;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.Node;
import com.google.ar.sceneform.math.Quaternion;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.schemas.lull.ModelPipelineRenderableDef;

import java.util.concurrent.CompletableFuture;

public class MyARNode extends AnchorNode {
    private AugmentedImage image;
    private static CompletableFuture <ModelRenderable> modelRenderableDefCompletableFuture;

    public MyARNode(Context context, int modelID){
        if(modelRenderableDefCompletableFuture == null){
            modelRenderableDefCompletableFuture= ModelRenderable.builder().setRegistryId("my_model")
                    .setSource(context,modelID).build();

        }
    }

    public void setImage(final AugmentedImage image) {
        this.image = image;
        if(!modelRenderableDefCompletableFuture.isDone()){
             CompletableFuture.allOf(modelRenderableDefCompletableFuture)
                     .thenAccept((Void avoid) ->{
                        setImage(image);
                     }).exceptionally(throwable -> {
                         return null;
             });
        }

        setAnchor(image.createAnchor(image.getCenterPose()));
        Node node = new Node();
        Pose pose = Pose.makeTranslation(0.0f,0.0f,0.25f);
        node.setParent(this);
        node.setLocalPosition(new Vector3(pose.tx(),pose.ty(),pose.tz()));
        node.setLocalRotation(new Quaternion(pose.qx(),pose.qy(),pose.qz(),pose.qw()));
        node.setRenderable(modelRenderableDefCompletableFuture.getNow(null));
    }

    public AugmentedImage getImage() {
        return image;
    }
}
