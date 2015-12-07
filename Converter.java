package Com.Company;
import java.awt.Color;
import java.awt.image.BufferedImage;
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
///////This is a converter that changes each pixel into the median of the neighboring pixels around it in a kernel///////
///////Made by MED3-6////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class Converter {

    // receives the image and the kernel that has been decided from client.
    public static BufferedImage convert(BufferedImage image, int kernelChoice) {

        //pixels in kernel to one side of the handled pixel
        int kernel = (kernelChoice-1)/2;

        //Calculates the amount of pixels being used in in the kernel
        int kernelSum = (kernelChoice*kernelChoice);

        //Runs for each pixel in the pictures height
        for (int y = 0; y < image.getHeight(); y++) {

            //Runs for each pixel in the pictures width
            for (int x = 0; x < image.getWidth(); x++) {
                ////////////////////////////////////////////////////////////////////////////////////
                /////////////////////////DEFINING THE CONTENT OF THE KERNEL/////////////////////////
                ////////////////////////////////////////////////////////////////////////////////////

                //Makes four arrays of ints with the amount being the same as the amount of pixels in the kernel
                int colorsRGB[] = new int[kernelSum];
                int mixedNumbersRed[] = new int[kernelSum];
                int mixedNumbersGreen[] = new int[kernelSum];
                int mixedNumbersBlue[] = new int[kernelSum];

                //Creates an array of Color with the amount being the same as the amount of pixels in the kernel
                Color colorArray[] = new Color[kernelSum];

                //creates and int that is the same value as kernelSum
                int arrayAmount = kernelSum;

                //creates two booleans
                boolean maybeFalse;
                boolean maybeTrue;

                //creates a loop that runs for each pixel amount in kernel height
                for (int i = 0; i < kernelChoice; i++) {

                    //Start every run as true
                    maybeFalse = true;

                    //if the horizontal line of pixels are above or below the picture then run this
                    if (y <= kernel-1 && i <= kernel || y >= image.getHeight()-kernel && i >= (kernelChoice+1)/2) {

                        //makes the sum of the pixels in the arrayAmount smaller by one line of the kernel
                        arrayAmount = arrayAmount - kernelChoice;

                        //Makes it so the 'if' sentence below will not run
                        maybeFalse = false;
                    }
                    //if the pixel is inside the y axis of the picture this will run
                    if (maybeFalse == true) {

                        //Creates a loop that runs for each pixel in kernel width
                        for (int j = 0; j < kernelChoice; j++) {

                            //Start every run as true
                            maybeTrue = true;

                            //if the pixel is outside the picture to the left or right then run this
                            if (x <= kernel-1 && j <= kernel || x >= image.getWidth()-kernel && j >= (kernelChoice+1)/2) {

                                //lower the sum of pixels in arrayAmount by 1
                                arrayAmount = arrayAmount - 1;

                                //Makes it so the 'if' sentence below will not run
                                maybeTrue = false;
                            }
                            //if the pixel is inside the x-axis of the picture this will run
                            if (maybeTrue == true) {

                                //Creates an int that is equal to the amount of pixels in the kernel so far
                                //so that the next number in the array is one higher each time this runs
                                int arraySize = i*kernelChoice + j;

                                //sets the array equal to the RGB information in a specific pixel, a number
                                colorsRGB[arraySize] = image.getRGB((x + j - kernel), (y + i - kernel));

                                //splits the pixels RGB information into a number for each, Red, Green and Blue
                                colorArray[arraySize] = new Color(colorsRGB[arraySize]);

                                //Gives each colour its own array
                                mixedNumbersRed[arraySize] = colorArray[arraySize].getRed();
                                mixedNumbersGreen[arraySize] = colorArray[arraySize].getGreen();
                                mixedNumbersBlue[arraySize] = colorArray[arraySize].getBlue();
                            }
                        }
                    }
                }
                ////////////////////////////////////////////////////////////////////////////////////////////////////
                //////CHANGES THE POSITION OF EACH COLOUR IN THE ARRAYS SO THAT THEY GO FROM LOWEST TO HIGHEST//////
                ////////////////////////////////////////////////////////////////////////////////////////////////////

                //starts a loop that runs so that it is possible for the last
                // number that might be the lowest to move to the lowest array number
                for(int i = 0; i < kernelSum; i++) {
                    //Starts a loop that runs for every array number, starting from the second array number
                    for (int j = 1; j < kernelSum; j++) {
                        //creates an int so we don't have to do the calculation every time
                        int j2 = j - 1;

                        //if the array int is lower than the array int before it then run
                        if (mixedNumbersRed[j] < mixedNumbersRed[j2]) {

                            //creates an int with the same value as the array int
                            int number = mixedNumbersRed[j];

                            //changes the array int to be the same as the one before it
                            mixedNumbersRed[j] = mixedNumbersRed[j2];

                            //changes the array int before it to be the same value as it was
                            mixedNumbersRed[j2] = number;
                        }
                        //same as with red above
                        if (mixedNumbersGreen[j] < mixedNumbersGreen[j2]) {
                            int number = mixedNumbersGreen[j];
                            mixedNumbersGreen[j] = mixedNumbersGreen[j2];
                            mixedNumbersGreen[j2] = number;
                        }
                        //same as with red above
                        if (mixedNumbersBlue[j] < mixedNumbersBlue[j2]) {
                            int number = mixedNumbersBlue[j];
                            mixedNumbersBlue[j] = mixedNumbersBlue[j2];
                            mixedNumbersBlue[j2] = number;
                        }
                    }
                }
                /////////////////////////////////////////////////////////////////////////////////////////////
                //////////FINDING THE MEDIAN COLOURS OF THE KERNEL AND CHANGING COLOUR VALUES TO IT//////////
                /////////////////////////////////////////////////////////////////////////////////////////////

                //creates an int that is equal to the amount of pixels that is not used
                int arrayAmountTwo = kernelSum - arrayAmount;

                //runs if the amount of pixels in the kernel is odd
                if (arrayAmount % 2 != 0) {
                    //makes the int even
                    arrayAmount = arrayAmount - 1;
                }

                //creates an int that is equal to the array position that is the median
                int medianEdge = arrayAmountTwo + arrayAmount / 2;

                //creates three ints that are equal to the median value of each colour
                int red = mixedNumbersRed[medianEdge];
                int green = mixedNumbersGreen[medianEdge];
                int blue = mixedNumbersBlue[medianEdge];

                //sets the colour of the pixel to have the median values of the kernel
                image.setRGB(x, y, new Color(red, green, blue).getRGB());
            }
        }
        //returns the edited image to the server
        return image;
    }
}