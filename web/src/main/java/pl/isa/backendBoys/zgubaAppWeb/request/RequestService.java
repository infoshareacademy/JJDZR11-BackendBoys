package pl.isa.backendBoys.zgubaAppWeb.request;

import org.springframework.stereotype.Component;

import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class RequestService {

    private final RequestDatabase requestDatabase;

    public RequestService(RequestDatabase requestDatabase) {
        this.requestDatabase = requestDatabase;
    }

    public void addRequest(String requesterLogin, Request.LostOrFound lostOrFound, String objectName, String description, String city) {
        requestDatabase.add(new Request(requesterLogin, lostOrFound, objectName, description, city));
    }

    public void addRequest(Request request) {
        requestDatabase.add(request);
        exportRequestDatabaseToJson();
    }

    public List<Request> getAllRequests() {
        return requestDatabase.getRequests();
    }

    public void changeRequesterLogin(String currentLoginEmail, String newLoginEmail) {
        requestDatabase.getRequests().stream()
                .filter(request -> request.getRequesterLogin()
                        .equals(currentLoginEmail))
                .forEach(request -> request.setRequesterLogin(newLoginEmail));

        exportRequestDatabaseToJson();

    }

    public boolean deleteRequestsByLogin(String loginEmail) {

        boolean isRequestFound = false;

        Iterator<Request> iterator = requestDatabase.getRequests().iterator();
        while (iterator.hasNext()) {
            Request request = iterator.next();
            if (request.getRequesterLogin().equals(loginEmail)) {
                iterator.remove();
                isRequestFound = true;
            }
        }
        return isRequestFound;
    }

    public List<Request> getRequestByloginEmail(String loginEmail) {
        return requestDatabase.getRequests().stream()
                .filter(request -> request.getRequesterLogin()
                        .equals(loginEmail)).collect(Collectors.toList());
    }

    public void exportRequestDatabaseToJson() {
        requestDatabase.exportToJson();
    }
}