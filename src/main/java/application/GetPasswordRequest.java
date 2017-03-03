package application;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class GetPasswordRequest {
  private String oldPassword, newPassword;

  @JsonCreator
  public GetPasswordRequest(@JsonProperty("oldPassword") String oldPassword,
                            @JsonProperty("newPassword") String newPassword)
  {
    this.oldPassword = oldPassword;
    this.newPassword = newPassword;
  }

  public String getOldPassword()
  {
    return oldPassword;
  }

  public String getNewPassword()
  {
    return newPassword;
  }
}