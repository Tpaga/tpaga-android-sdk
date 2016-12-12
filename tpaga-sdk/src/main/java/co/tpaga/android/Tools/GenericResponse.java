package co.tpaga.android.Tools;

public class GenericResponse {

    public StatusResponse status;


    public static GenericResponse create(StatusResponse statusResponse) {
        GenericResponse genericResponse = new GenericResponse();
        genericResponse.status = statusResponse;
        return genericResponse;
    }
}
