package com.INSE;

import com.InterfaceService;

import javax.xml.namespace.QName;
import javax.xml.ws.Endpoint;
import javax.xml.ws.Service;
import java.io.IOException;
import java.net.*;

public class InsePublisher {

    public static URL INSE_url;
    public static InterfaceService INSEObj;

    public static void main(String[] args) throws IOException, InterruptedException

    {

        Runnable task1 = () -> {
            try {
                StartServer(args);
            } catch (IOException e) {
                e.printStackTrace();
            }

        };

        Runnable task2 = () -> {
            try {
                receive();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

        };

        Thread thread1 = new Thread(task1);
        Thread thread2 = new Thread(task2);
        thread1.start();
        thread2.start();
    }

    public static void StartServer(String args[]) throws IOException

    {
        Endpoint ep = Endpoint.create(new ImplINSE());

        ep.publish("http://127.0.0.1:20000/INSE");
        System.out.println("INSE Web service started");


        INSE_url = new URL("http://127.0.0.1:20000/INSE?wsdl");

        QName qname = new QName("http://INSE.com/", "ImplINSEService");

        Service service = Service.create(INSE_url, qname);
        INSEObj = service.getPort(InterfaceService.class);
    }


    public static void receive() throws MalformedURLException {




        DatagramSocket aSocket = null;
        try {
            aSocket = new DatagramSocket(1414);
            byte[] buffer = new byte[1000];// to stored the received data from
            // the client.
            System.out.println("Server 1414 Started............");
            while (true) {// non-terminating loop as the server is always in
                // listening mode.
                DatagramPacket request = new DatagramPacket(buffer, buffer.length);
                // Server waits for the request to
                // come------------------------------------------------------------------
                aSocket.receive(request);// request received

                byte[] byt = request.getData();
                char [] message = new char[byt.length];

                for(int i=0; i< byt.length; i++)
                {
                    message[i] = (char) byt[i];
                }
                String str = String.valueOf(message);
                System.out.println(str.trim());

                String data= " ";
                if(str.trim().toLowerCase().equals("fall"))
                {
                    if(INSEObj.courseAvailability("fall").length>0)
                    {

                        for (int i = 0; i < INSEObj.courseAvailability("fall").length; i++) {
                            data += (INSEObj.courseAvailability("fall")[i])+ " ";
                        }
                    }

                }
                else if (str.trim().toLowerCase().equals("winter"))
                {
                    if(INSEObj.courseAvailability("winter").length>0)
                    {
                        for (int i = 0; i < INSEObj.courseAvailability("winter").length; i++) {
                            data += (INSEObj.courseAvailability("winter")[i])+" ";
                        }
                    }


                }
                else if (str.trim().toLowerCase().equals("summer"))
                {
                    if(INSEObj.courseAvailability("summer").length>0)
                    {
                        for (int i = 0; i < INSEObj.courseAvailability("summer").length; i++) {
                            data +=  (INSEObj.courseAvailability("summer")[i])+" ";
                        }
                    }

                }

                else
                {

                    data = "-0-";
                }

                DatagramPacket reply = new DatagramPacket(data.getBytes(), data.length(), request.getAddress(),
                        request.getPort());// reply packet ready
                aSocket.send(reply);// reply sent
            }
        } catch (SocketException e) {
            System.out.println("Socket: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("IO: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (aSocket != null)
                aSocket.close();
        }


    }
}
    //*************************UDP****************************//
