package ch.heigvd.res.labs.roulette.net.client;

import ch.heigvd.res.labs.roulette.data.EmptyStoreException;
import ch.heigvd.res.labs.roulette.data.JsonObjectMapper;
import ch.heigvd.res.labs.roulette.net.protocol.RouletteV1Protocol;
import ch.heigvd.res.labs.roulette.data.Student;
import ch.heigvd.res.labs.roulette.net.protocol.InfoCommandResponse;
import ch.heigvd.res.labs.roulette.net.protocol.RandomCommandResponse;
import java.io.*;
import java.net.Socket;
import java.util.List;
//import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class implements the client side of the protocol specification (version 1).
 *
 * @author Olivier Liechti
 */
public class RouletteV1ClientImpl implements IRouletteV1Client {

   private static final Logger LOG = Logger.getLogger(RouletteV1ClientImpl.class.getName());

   Socket clientSocket;
   InputStream fromServer;
   OutputStream toServer;
   private static byte[] buffer;

   @Override
   public void connect(String server, int port) throws IOException {
      clientSocket = new Socket(server, port);
      fromServer = clientSocket.getInputStream();
      toServer = clientSocket.getOutputStream();
      buffer = new byte[200];

      clr();
   }

   @Override
   public void disconnect() throws IOException {
      fromServer.close();
      toServer.close();
      clientSocket.close();
   }

   @Override
   public boolean isConnected() {
      return (clientSocket != null && clientSocket.isConnected());
   }

   @Override
   public void loadStudent(String fullname) throws IOException {
      toServer.write((RouletteV1Protocol.CMD_LOAD + "\n").getBytes());
      clr();

      toServer.write((fullname + "\n").getBytes());

      toServer.write((RouletteV1Protocol.CMD_LOAD_ENDOFDATA_MARKER + "\n").getBytes());
      clr();
   }

   @Override
   public void loadStudents(List<Student> students) throws IOException {
      toServer.write((RouletteV1Protocol.CMD_LOAD).getBytes());
      clr();

      for (Student s : students) {
         toServer.write((s.getFullname() + "\n").getBytes());
      }

      toServer.write((RouletteV1Protocol.CMD_LOAD_ENDOFDATA_MARKER + "\n").getBytes());
      clr();
   }

   @Override
   public Student pickRandomStudent() throws EmptyStoreException, IOException {
      toServer.write((RouletteV1Protocol.CMD_RANDOM + "\n").getBytes());
      Student returnStudent = new Student();
      clr();

      RandomCommandResponse response = JsonObjectMapper.parseJson(new String(buffer), RandomCommandResponse.class);
      if (!response.getError().isEmpty()) {
         throw new EmptyStoreException();
      }

      returnStudent.setFullname(response.getFullname());

      return returnStudent;
   }

   @Override
   public int getNumberOfStudents() throws IOException {
      toServer.write((RouletteV1Protocol.CMD_INFO + "\n").getBytes());
      clr();

      InfoCommandResponse response = JsonObjectMapper.parseJson(new String(buffer), InfoCommandResponse.class);
      return response.getNumberOfStudents();
   }

   @Override
   public String getProtocolVersion() throws IOException {
      toServer.write((RouletteV1Protocol.CMD_INFO + "\n").getBytes());
      clr();

      InfoCommandResponse response = JsonObjectMapper.parseJson(new String(buffer), InfoCommandResponse.class);
      return response.getProtocolVersion();
   }

   protected void clr() throws IOException {
      fromServer.read(buffer);
   }
}
