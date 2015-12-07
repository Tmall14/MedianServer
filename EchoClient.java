package Com.Company;

/**
 * Created by Thomas on 18-11-2015.
 */
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.Socket;
import java.nio.ByteBuffer;

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////initiates the server and establishes a connection to port 1234 ///////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class EchoClient {
    private String hostName;
    private int portNumber;


    public static void main(String[] args) throws IOException {
        // initiates the GUI version
        ActorGUIVersion.main();
    }

    public EchoClient(int kernelChoice) {
        // describes the client and what port it is supposed to connect to.
        this.hostName = "localhost";
        this.portNumber = 1234;
        try {
            //makes a connection available
            client(new Socket(hostName, portNumber), kernelChoice);
        }

        catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    public void client(Socket s, int kernelChoice) {

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////makes the communication and sends the necessary data /////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        //makes sure that it can send and receive data.
        OutputStream out;
        InputStream in = null;
        try {
            //Getting IO's
            out = s.getOutputStream();
            in = s.getInputStream();
            System.out.println("Got image IO connection to server..");
            // prints the number you've selected
            System.out.println(kernelChoice);
            //Getting image location
            JFileChooser jfc = new JFileChooser();
            jfc.setDialogTitle("Select image file..");
            int action = jfc.showOpenDialog(null);
            if (action != JFileChooser.APPROVE_OPTION)
                System.exit(0); // canceled by user
            // uploads the image
            File f = jfc.getSelectedFile();

            //Feedback
            System.out.println("Got file " + f.getName());

            //Storing image as bufferedimage
            BufferedImage bi = ImageIO.read(f);

            //Creating byte array that represent the image
            ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
            ImageIO.write(bi, "jpg", byteOut);

            //creates packages that represent image and the kernel choice
            byte[] size = ByteBuffer.allocate(4).putInt(byteOut.size()).array();
            byte[] kernel = ByteBuffer.allocate(4).putInt(kernelChoice).array();
            System.out.println("Sending image to server.");
            //Sends the image and kernel
            out.write(size);
            out.write(kernel);
            out.write(byteOut.toByteArray());
            out.flush();


////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////// receives the new image that has been converted and displays it as output.jpg ////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            System.out.println("Waiting for converted image.");
            //Reading the bytes from server
            byte[] sizeAr = new byte[4];
            in.read(sizeAr);
            // turning the byte into an int
            int cSize = ByteBuffer.wrap(sizeAr).asIntBuffer().get();
            //defines the size of the image that was returned
            byte[]imageAr = new byte[cSize];
            int sizerecv = 0;
            int sizerecv2 = 0;
            // loads the image data
            byte[] imageAr2 = new byte[cSize];
            int placement = 0;
            while(sizerecv < cSize){
                sizerecv2 = in.read(imageAr);
                for(int i = 0; i < sizerecv2; i++)
                {
                    imageAr2[i + placement] = imageAr[i];

                }
                placement = placement + sizerecv2;
                sizerecv = sizerecv + sizerecv2;
            }
            System.out.println("Got converted image data from server, starting conversion to image.");

            //Converting bytes to the image
            BufferedImage convertedBI = null;
            try {
                convertedBI = ImageIO.read(new ByteArrayInputStream(imageAr2));
            }
            catch(EOFException e) {
                System.out.println("Finished reading the image.");
            }
            System.out.println("Writing file to output.jpg.");

            //Saving the converted image
            ImageIO.write(convertedBI, "jpg", new File("output.jpg"));
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            try {
                // shuts down the program
                in.close();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}