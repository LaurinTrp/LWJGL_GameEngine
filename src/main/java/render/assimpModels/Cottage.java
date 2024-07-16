package main.java.render.assimpModels;

import org.lwjgl.glfw.GLFW;

import glm.vec._3.Vec3;
import main.java.data.Material;
import main.java.data.sounds.AudioData;
import main.java.data.sounds.SoundSource;
import main.java.gui.Engine_Main;
import main.java.model.AssimpModel;
import main.java.model.objects.Mesh;
import main.java.utils.loaders.AssimpModelLoader;
import main.java.utils.loaders.ImageLoader;

public class Cottage extends AssimpModel {

	private AudioData audio;
	private SoundSource source;
	
	public Cottage() {
		super(AssimpModelLoader.loadStaticFromResource("Cottage", "Cottage_w_Light.dae"));
		this.material = new Material(ImageLoader.loadTextureFromResource("cottage", "cottage_diffuse.png"));
	
		audio = new AudioData("/run/media/laurin/Festplatte/Programmieren/Java/3D-Workbench/LWJGL_GameEngineResource/src/resources/audio/bounce.ogg");
		Engine_Main.soundManager.addAudioData("Loopmusic", audio);
		
		source = new SoundSource(true, false);
		source.setAudioData(audio);
		Engine_Main.soundManager.addSoundSource("Cottage", source);
		
	}

	@Override
	protected void renderProcessBegin() {
		if(Engine_Main.keyHandler.isPressed(GLFW.GLFW_KEY_LEFT)) {
			modelMatrix.translate(1f, 0, 0);
		}
		if(Engine_Main.keyHandler.isPressed(GLFW.GLFW_KEY_RIGHT)) {
			modelMatrix.translate(-1f, 0, 0);
		}
	}
	
	@Override
	protected void renderProcessEnd() {
	}

	@Override
	public void afterInit() {
//		modelMatrix.translate(new Vec3(10f, 1f, 10f));

		source.setPosition(0, 0, 0);
		source.setGain(2f);
		source.play();
	}

	@Override
	public void clicked(Mesh mesh) {
		if(!source.isPlaying()) {
			source.play();
		}
	}
}
