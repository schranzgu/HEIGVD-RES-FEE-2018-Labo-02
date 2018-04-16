package ch.heigvd.res.labs.roulette.net.client;

import ch.heigvd.res.labs.roulette.data.JsonObjectMapper;
import ch.heigvd.res.labs.roulette.data.Student;
import ch.heigvd.res.labs.roulette.data.StudentsList;
import ch.heigvd.res.labs.roulette.net.protocol.RouletteV2Protocol;
import java.io.IOException;
import java.util.List;
import java.io.ByteArrayOutputStream;

/**
 * This class implements the client side of the protocol specification (version 2).
 *
 * @author Olivier Liechti
 */
public class RouletteV2ClientImpl extends RouletteV1ClientImpl implements IRouletteV2Client {

   @Override
   public void clearDataStore() throws IOException {
      toServer.write((RouletteV2Protocol.CMD_CLEAR + "\n").getBytes());
   }

   @Override
   public List<Student> listStudents() throws IOException {
      toServer.write((RouletteV2Protocol.CMD_LIST + '\n').getBytes());
      toServer.flush();

      ByteArrayOutputStream response = new ByteArrayOutputStream();
      response.write(buffer, 0, fromServer.read(buffer));
      StudentsList list = JsonObjectMapper.parseJson(response.toString(), StudentsList.class);

      return list.getStudents();
   }

   @Override
   public String getProtocolVersion() throws IOException {
      return RouletteV2Protocol.VERSION;
   }
}
