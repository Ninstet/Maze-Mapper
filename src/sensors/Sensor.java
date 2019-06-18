package sensors;

import lejos.robotics.SampleProvider;

public class Sensor {
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
