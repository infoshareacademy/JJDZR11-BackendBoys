package pl.isa.backendBoys.zgubaAppWeb.request;

import org.springframework.stereotype.Component;
import pl.isa.backendBoys.zgubaAppWeb.database.JsonService;

import java.util.ArrayList;
import java.util.List;

@Component
public class RequestDatabase {
    private final List<Request> requests = new ArrayList<>(JsonService.getRequestsfromJsonFile());

    public List<Request> getRequests() {
        return requests;
    }

    public void add(Request request) {
        requests.add(request);
    }

    public void exportToJson() {
        JsonService.updateRequestsJsonFile(requests);
    }
}
