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

public class EchoClient {
    private String hostName;
    private int portNumber;


    public static void main(String[] args) throws IOException {
        ActorGUIVersion.main();
    }

    public EchoClient(int kernelChoice) {
        this.hostName = "localhost";
        this.portNumber = 1234;
        try {
            client(new Socket(hostName, portNumber), kernelChoice);
        }
        catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    public void client(Socket s, int kernelChoice) {
        OutputStream out;
        InputStream in = null;
        try {
            //Getting IO's
            out = s.getOutputStream();
            in = s.getInputStream();
            System.out.println("Got image IO connection to server..");

            System.out.println(kernelChoice);
            //Getting image:

            JFileChooser jfc = new JFileChooser();
            jfc.setDialogTitle("Select image file..");
            int action = jfc.showOpenDialog(null);
            if (action != JFileChooser.APPROVE_OPTION)
                System.exit(0); // canceled by user

            File f = jfc.getSelectedFile();

            //Feedback
            System.out.println("Got file " + f.getName());

            //Storing image as bufferedimage
            BufferedImage bi = ImageIO.read(f);

            //Creating byte array that represent the image
            ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
            ImageIO.write(bi, "jpg", byteOut);

            byte[] size = ByteBuffer.allocate(4).putInt(byteOut.size()).array();
            byte[] kernel = ByteBuffer.allocate(4).putInt(kernelChoice).array();
            System.out.println(byteOut.size());
            System.out.println("Sending image to server.");
            //Sends the image
            System.out.println(size);
            out.write(size);
            out.write(kernel);
            out.write(byteOut.toByteArray());
            out.flush();

            System.out.println("Waiting for converted image.");
            //Reading the bytes from server
            byte[] sizeAr = new byte[4];
            in.read(sizeAr);
            int cSize = ByteBuffer.wrap(sizeAr).asIntBuffer().get();

            byte[]imageAr = new byte[cSize];
            int sizerecv = 0;
            int sizerecv2 = 0;

            byte[] imageAr2 = new byte[cSize];
            int placement = 0;
            while(sizerecv < cSize){
                sizerecv2 = in.read(imageAr);
                for(int i = 0; i < sizerecv2; i++)
                {
                    imageAr2[i + placement] = imageAr[i];

                }
                placement = placement + sizerecv2;
                System.out.println(imageAr[1]);
                System.out.println(imageAr2[1]);
                sizerecv = sizerecv + sizerecv2;
            }
            //in.read(imageAr);
            //int sizerecv = 0;
            //while(sizerecv == in.read(imageAr));
            //in.read(imageAr);
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
                in.close();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}