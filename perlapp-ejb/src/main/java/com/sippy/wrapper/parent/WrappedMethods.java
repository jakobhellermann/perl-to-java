package com.sippy.wrapper.parent;

import com.sippy.wrapper.parent.database.DatabaseConnection;
import com.sippy.wrapper.parent.database.dao.TnbDao;
import com.sippy.wrapper.parent.request.GetTnbListRequest;
import com.sippy.wrapper.parent.request.JavaTestRequest;
import com.sippy.wrapper.parent.response.JavaTestResponse;
import com.sippy.wrapper.parent.response.TnbResponse;
import java.util.*;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Stateless
public class WrappedMethods {

  private static final Logger LOGGER = LoggerFactory.getLogger(WrappedMethods.class);

  @EJB DatabaseConnection databaseConnection;

  @RpcMethod(name = "getTnbList", description = "Get tnb list")
  public Map<String, Object> getTnbList(GetTnbListRequest request) {
    List<TnbDao> tnbsFromDb = databaseConnection.getAllTnbs();

    TnbDao tnb = null;
    if(request.getNumber() != null) {
      tnb = databaseConnection.getTnb(request.getNumber());
    }

    boolean isTnb001 = tnb != null && tnb.getTnb().equals("D001");
    List<TnbResponse> tnbs = new ArrayList<>(List.of(new TnbResponse("D001", "Deutsche Telekom", isTnb001)));

    for(TnbDao tnbFromDb : tnbsFromDb) {
      List<String> tnbsToIgnore = List.of("D146", "D218", "D248");
      if (tnbsToIgnore.stream().anyMatch(ignore -> tnbFromDb.getTnb().equals(ignore))) {
        continue;
      }
      boolean isTnb = tnb != null && tnb.getName().equals(tnbFromDb.getName());
      tnbs.add(new TnbResponse(tnbFromDb.getTnb(), tnbFromDb.getName(), isTnb));
    }

    tnbs.sort(Comparator.comparing((TnbResponse tnbResponse) -> tnbResponse.getName().toLowerCase()));

    Map<String, Object> jsonResponse = new HashMap<>();
    jsonResponse.put("faultCode", "200");
    jsonResponse.put("faultString", "Method success");
    jsonResponse.put("tnbs", tnbs);

    return jsonResponse;
  }


  @RpcMethod(name = "javaTest", description = "Check if everything works :)")
  public Map<String, Object> javaTest(JavaTestRequest request) {
    JavaTestResponse response = new JavaTestResponse();

    int count = databaseConnection.getAllTnbs().size();

    LOGGER.info("the count is: " + count);

    response.setId(request.getId());
    String tempFeeling = request.isTemperatureOver20Degree() ? "warm" : "cold";
    response.setOutput(
        String.format(
            "%s has a rather %s day. And he has %d tnbs", request.getName(), tempFeeling, count));

    Map<String, Object> jsonResponse = new HashMap<>();
    jsonResponse.put("faultCode", "200");
    jsonResponse.put("faultString", "Method success");
    jsonResponse.put("something", response);

    return jsonResponse;
  }
}
