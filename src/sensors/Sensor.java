package sensors;

import lejos.robotics.SampleProvider;
import main.Controller;

public abstract class Sensor {
	public static final Colour LEFT_COLOUR_SENSOR = new Colour(Controller.EV3_BRICK.getPort("S1"));
	public static final Colour RIGHT_COLOUR_SENSOR = new Colour(Controller.EV3_BRICK.getPort("S2"));
	public static final Gyro GYRO = new Gyro(Controller.EV3_BRICK.getPort("S3"));
	public static final IR IR_SENSOR = new IR(Controller.EV3_BRICK.getPort("S4"));
	
	public SampleProvider sampler;
	
	public static float[] average(SampleProvider sampler, int number) {
		float[] sample = new float[sampler.sampleSize()];
		float[] temp = new float[3];
		
		for (int i = 0; i < number; i++) {
			sampler.fetchSample(sample, 0);
			
			for (int j = 0; j < sampler.sampleSize(); j++) {
				temp[j] += sample[j] / number;
			}
		}
		return temp;
	}
	
}
