package color;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class HexGen {
	
	private int r;
	private int g;
	private int b;
	private BufferedImage image;
	public HexGen(File file) {
		try {
			image = ImageIO.read(file);
			r=0;
			g=0;
			b=0;
				int[] rgbVal = image.getRGB(image.getMinTileX(), image.getMinY(), image.getWidth(), image.getHeight(), null, 0, image.getWidth());			
				
				for (int i=0; i < rgbVal.length; i++) {
					Color c = new Color(rgbVal[i]);
					
					r += c.getRed();
					g += c.getGreen();
					b += c.getBlue();
				}
				r = r/rgbVal.length;
				g = g/rgbVal.length;
				b = b/rgbVal.length;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public String getHex() {
		return String.format("#%02X%02X%02X", r, g, b);
	}
	
	public String getCompliment() {
		int compR = 255 - r;
		int compG = 255 - g;
		int compB = 255 - b;
		return String.format("#%02X%02X%02X", compR, compG, compB);
	}
	
	public String getCompliment(String hex) {
		 Color color = Color.decode(hex);
		 r = color.getRed();
		 g = color.getGreen();
		 b = color.getBlue();
		 
		int compR = 255 - r;
		int compG = 255 - g;
		int compB = 255 - b;
		return String.format("#%02X%02X%02X", compR, compG, compB);
		 
	}

}
