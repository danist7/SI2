/**
 * Pr&aacute;ctricas de Sistemas Inform&aacute;ticos II
 * VisaCancelacionJMSBean.java
 */

package ssii2.visa;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.ejb.EJBException;
import javax.ejb.MessageDriven;
import javax.ejb.MessageDrivenContext;
import javax.ejb.ActivationConfigProperty;
import javax.jms.MessageListener;
import javax.jms.Message;
import javax.jms.TextMessage;
import javax.jms.JMSException;
import javax.annotation.Resource;
import java.util.logging.Logger;

/**
 * @author jaime
 */
@MessageDriven(mappedName = "jms/VisaPagosQueue")
public class VisaCancelacionJMSBean extends DBTester implements MessageListener {
  static final Logger logger = Logger.getLogger("VisaCancelacionJMSBean");
  @Resource
  private MessageDrivenContext mdc;

  private static final String UPDATE_CANCELA_QRY = "update pago set codRespuesta = 999 where idAutorizacion=?";
    private static final String DESHACER_PAGO_QRY = "update tarjeta "+
                                                    "set saldo = saldo + importe "+
                                                    " from pago where pago.idAutorizacion=? "+
                                                    " and pago.numeroTarjeta = tarjeta.numeroTarjeta";
   // TODO : Definir UPDATE sobre la tabla pagos para poner
   // codRespuesta a 999 dado un código de autorización


  public VisaCancelacionJMSBean() {
  }

  // TODO : Método onMessage de ejemplo
  // Modificarlo para ejecutar el UPDATE definido más arriba,
  // asignando el idAutorizacion a lo recibido por el mensaje
  // Para ello conecte a la BD, prepareStatement() y ejecute correctamente
  // la actualización
  public void onMessage(Message inMessage) {
      TextMessage msg = null;
      Connection con = null;
      PreparedStatement pstmt = null;
      int id;
      try {
          if (inMessage instanceof TextMessage) {
              msg = (TextMessage) inMessage;
              logger.info("MESSAGE BEAN: Message received: " + msg.getText());

              id = Integer.parseInt(msg.getText());

              // Obtener conexion
              con = getConnection();

              // Actualizamos el codigo de respuesta del pago
              logger.info(UPDATE_CANCELA_QRY);
              pstmt = con.prepareStatement(UPDATE_CANCELA_QRY);
              pstmt.setInt(1,id);
              pstmt.execute();

              // Deshacemos el pago
              logger.info(DESHACER_PAGO_QRY);
              pstmt = con.prepareStatement(DESHACER_PAGO_QRY);
              pstmt.setInt(1,id);
              pstmt.execute();

          } else {
              logger.warning(
                      "Message of wrong type: "
                      + inMessage.getClass().getName());
          }
      } catch (JMSException e) {
          e.printStackTrace();
          mdc.setRollbackOnly();
      } catch (Throwable te) {
          te.printStackTrace();
      }
  }


}
