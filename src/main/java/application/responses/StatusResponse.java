package application.responses;

public class StatusResponse {
  private String status;

  public StatusResponse(String status) {
    this.status = status;
  }

  public String getStatus() {
    return status;
  }
}
