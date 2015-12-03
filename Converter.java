package Com.Company;
/**
 * Created by Thomas on 18-11-2015.
 */
import java.awt.Color;
import java.awt.image.BufferedImage;

public class Converter {
    public static BufferedImage convert(BufferedImage image, int kernelChoice) {
        int kernel = (kernelChoice-1)/2;
        int kernelSum = (kernelChoice*kernelChoice);
        int medianKernel = (kernelSum-1)/2;

        for (int y = kernel; y < image.getHeight() - kernel; y++) {
            for (int x = kernel; x < image.getWidth() - kernel; x++) {

                int colorsRGB[] = new int[kernelSum];
                Color colorArray[] = new Color[kernelSum];
                int mixedNumbersRed[] = new int[kernelSum];
                int mixedNumbersGreen[] = new int[kernelSum];
                int mixedNumbersBlue[] = new int[kernelSum];

                for(int i = 0; i < kernelChoice; i++){
                    for(int j = 0; j < kernelChoice; j++){
                        int arraySize = i*kernelChoice + j;
                        colorsRGB[arraySize] = image.getRGB((x + j - kernel), (y + i - kernel));
                        colorArray[arraySize] = new Color(colorsRGB[arraySize]);
                        mixedNumbersRed[arraySize] = colorArray[arraySize].getRed();
                        mixedNumbersGreen[arraySize] = colorArray[arraySize].getGreen();
                        mixedNumbersBlue[arraySize] = colorArray[arraySize].getBlue();
                    }
                }

                for(int i = 0; i < kernelSum; i++) {
                    for (int j = 1; j < kernelSum; j++) {
                        int j2 = j - 1;
                        if (mixedNumbersRed[j] < mixedNumbersRed[j2]) {
                            int number = mixedNumbersRed[j];
                            mixedNumbersRed[j] = mixedNumbersRed[j2];
                            mixedNumbersRed[j2] = number;
                        }
                        if (mixedNumbersGreen[j] < mixedNumbersGreen[j2]) {
                            int number = mixedNumbersGreen[j];
                            mixedNumbersGreen[j] = mixedNumbersGreen[j2];
                            mixedNumbersGreen[j2] = number;
                        }
                        if (mixedNumbersBlue[j] < mixedNumbersBlue[j2]) {
                            int number = mixedNumbersBlue[j];
                            mixedNumbersBlue[j] = mixedNumbersBlue[j2];
                            mixedNumbersBlue[j2] = number;
                        }
                    }
                }

                int red = mixedNumbersRed[medianKernel];
                int green = mixedNumbersGreen[medianKernel];
                int blue = mixedNumbersBlue[medianKernel];

                image.setRGB(x, y, new Color(red, green, blue).getRGB());
            }
        }
        return image;
    }
}
