package Com.Company;

/**
 * Created by Thomas on 18-11-2015.
 */

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;

public class MultiThreadServer  {
    ServerSocket ss;
    public MultiThreadServer() {
        try {
            ServerSocket ss = new ServerSocket(1234);
            System.out.println("Listening");
            while (true) {
                Socket s;

                s = ss.accept();
                Client c = new Client(s);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String args[]) throws Exception {
        new MultiThreadServer();
    }

    public class Client extends Thread implements Runnable {
        Socket s;

        public Client(Socket s) {
            this.s = s;
            this.start();
        }

        @Override
        public void run() {
            try {
                OutputStream out = s.getOutputStream();
                InputStream in = s.getInputStream();
                System.out.println("Got image IO connections to client");

                //Reading the bytes from client
                byte[] sizeAr = new byte[4];

                in.read(sizeAr);
                int size = ByteBuffer.wrap(sizeAr).asIntBuffer().get();
                System.out.println("Getting image from client!");

                byte[] kernel = new byte[4];
                in.read(kernel);
                int kernelChoice = ByteBuffer.wrap(kernel).asIntBuffer().get();
                System.out.println(kernelChoice);

                byte[] imageAr = new byte[size];
                int sizerecv = 0;
                int sizerecv2 = 0;

                /*while(sizerecv != size){
                    sizerecv = sizerecv + in.read(imageAr);
                    System.out.println(sizerecv);
                    System.out.println(size);
                    loop++;
                }*/
                byte[] imageAr2 = new byte[size];
                int placement = 0;
                while(sizerecv < size){
                    sizerecv2 = in.read(imageAr);
                    for(int i = 0; i < sizerecv2; i++)
                    {
                        imageAr2[i + placement] = imageAr[i];

                    }
                    placement = placement + sizerecv2;
                //    System.out.println(imageAr[1]);
                //    System.out.println(imageAr2[1]);
                    sizerecv = sizerecv + sizerecv2;
                }
                /*int loop = 0;
                int infoArray[] = new int[30];
                for(int sizerecv = in.read(imageAr); sizerecv != size; sizerecv = sizerecv + in.read(imageAr)){
                    loop++;
                    infoArray[loop] = sizerecv;
                    System.out.println(sizerecv);
                }
                System.out.println(infoArray[3]);

                System.out.println(loop);
                for(int i = 0; i < loop; i++){
                    in.read(imageAr);
                }*/

                System.out.println("CONTINUE!");

                System.out.println("Read image data, converting to image.");

                //Converting bytes to the image

                BufferedImage bi = null;
                try {
                    bi = ImageIO.read(new ByteArrayInputStream(imageAr2));
                }
                catch(EOFException e) {
                    System.out.println("Finished reading the image.");
                }

                System.out.println("Starting converting program.");
                //Converting the image
                BufferedImage converted = Converter.convert(bi, kernelChoice);

                System.out.println("Done, sending image back to client.");

                //Sending converted image back to client

                //Creating byte array that represent the converted image
                ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
                ImageIO.write(converted, "jpg", byteOut);

                byte[] cSize = ByteBuffer.allocate(4).putInt(byteOut.size()).array();

                //Sends the image
                out.write(cSize);
                out.write(byteOut.toByteArray());
                out.flush();

                in.close();
                out.close();
                s.close();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}