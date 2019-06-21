package sensors;

import lejos.robotics.SampleProvider;

public abstract class Sensor {
//	public static final Sensor LEFT_COLOUR_SENSOR = new Colour(Controller.EV3_BRICK.getPort("S1"));
//	public static final Sensor RIGHT_COLOUR_SENSOR = new Colour(Controller.EV3_BRICK.getPort("S2"));
//	public static final Sensor GYRO = new Gyro(Controller.EV3_BRICK.getPort("S3"));
//	public static final Sensor IR_SENSOR = new IR(Controller.EV3_BRICK.getPort("S4"));
	
	public static final Sensor SIMULATE = new Simulate();
	
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
