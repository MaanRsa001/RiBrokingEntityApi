package com.maan.insurance.auth.bean;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class LoginMasterID implements Serializable{
	private static final long serialVersionUID = 1L;
	@Column(name="LOGIN_ID")
	private String loginid;
	@Column(name="PASSWORD")
	private String password;
	
	/*@Column(name="LOGIN_ID")
	public String getLoginid() {
		return loginid;
	}
	@Column(name="LOGIN_ID")
	public void setLoginid(String loginid) {
		this.loginid = loginid;
	}
	@Column(name="PASSWORD")
	public String getPassword() {
		return password;
	}
	@Column(name="PASSWORD")
	public void setPassword(String password) {
		this.password = password;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((loginid == null) ? 0 : loginid.hashCode());
		result = prime * result + ((password == null) ? 0 : password.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		LoginMasterID other = (LoginMasterID) obj;
		if (loginid == null) {
			if (other.loginid != null)
				return false;
		} else if (!loginid.equals(other.loginid))
			return false;
		if (password == null) {
			if (other.password != null)
				return false;
		} else if (!password.equals(other.password))
			return false;
		return true;
	}
	*/
	
}
