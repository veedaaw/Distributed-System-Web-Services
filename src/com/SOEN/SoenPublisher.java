package com.SOEN;

import com.COMP.ImplCOMP;
import com.InterfaceService;

import javax.xml.namespace.QName;
import javax.xml.ws.Endpoint;
import javax.xml.ws.Service;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.net.URL;

public class SoenPublisher
{
    public static URL SOEN_url;
    public static InterfaceService SOENObj;

    public static void main(String[] args) throws IOException

    {


        Runnable task1 = () -> {
            try {
                StartServer(args);
            } catch (IOException e) {
                e.printStackTrace();
            }
        };

        Runnable task2 = () -> {
            receive();
        };

        Thread thread1 = new Thread(task1);
        Thread thread2 = new Thread(task2);
        thread1.start();
        thread2.start();


    }

    public static void StartServer(String args[]) throws IOException

    {
        Endpoint ep = Endpoint.create(new ImplSOEN());

        System.out.println("SOEN Web service started");

        ep.publish("http://127.0.0.1:30000/SOEN");

        SOEN_url = new URL("http://127.0.0.1:30000/SOEN?wsdl");

        QName _qname_ = new QName("http://SOEN.com/", "ImplSOENService");

        Service _service_ = Service.create(SOEN_url, _qname_);
        SOENObj = _service_.getPort(InterfaceService.class);
    }


    public static void receive()
    {

        DatagramSocket aSocket = null;
        try {
            aSocket = new DatagramSocket(1313);
            byte[] buffer = new byte[1000];// to stored the received data from
            // the client.
            System.out.println("Server 1313 Started....");
            while (true) {// non-terminating loop as the server is always in
                // listening mode.
                DatagramPacket request = new DatagramPacket(buffer, buffer.length);
                // Server waits for the request to
                // come------------------------------------------------------------------
                aSocket.receive(request);// request received

                byte[] byt = request.getData();
                char[] message = new char[byt.length];

                for (int i = 0; i < byt.length; i++) {
                    message[i] = (char) byt[i];
                }
                String str = String.valueOf(message);
                System.out.println(str.trim());

                String data = " ";
                if (str.trim().toLowerCase().equals("fall")) {
                    if( SOENObj.courseAvailability("fall").length>0) {
                        for (int i = 0; i < SOENObj.courseAvailability("fall").length; i++) {
                            data += (SOENObj.courseAvailability("fall")[i])+ " ";
                        }

                    }


                }
                else if (str.trim().toLowerCase().equals("winter")) {
                    if( SOENObj.courseAvailability("winter").length>0) {
                        for (int i = 0; i < SOENObj.courseAvailability("winter").length; i++) {
                            data += (SOENObj.courseAvailability("winter")[i]) + " ";
                        }
                    }

                }
                else if (str.trim().toLowerCase().equals("summer")) {
                    if( SOENObj.courseAvailability("summer").length>0)
                    {
                        for (int i = 0; i < SOENObj.courseAvailability("summer").length; i++) {
                            data += (SOENObj.courseAvailability("summer")[i])+ " ";
                        }
                    }

                }
                else {
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
