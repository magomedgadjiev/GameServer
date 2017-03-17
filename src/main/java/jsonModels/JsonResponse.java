package jsonModels;

/**
 * Created by magomed on 17.03.17.
 */
public class JsonResponse {
    private Resp response;

    public JsonResponse(Resp response) {
        this.response = response;
    }

    public Resp getResponse() {
        return response;
    }

    public void setResponse(Resp response) {
        this.response = response;
    }
}
