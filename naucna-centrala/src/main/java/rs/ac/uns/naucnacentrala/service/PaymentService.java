package rs.ac.uns.naucnacentrala.service;


public interface PaymentService {

    void createLinks(Long casopisId);

    String completeRegistration(String uuid, Long nacinPlacanjaId);

    String getRedirectUrl(String uapId, String username, String processInstanceId);

    String changePayed(String uuid, Boolean success, String username, String processInstanceId);
}
