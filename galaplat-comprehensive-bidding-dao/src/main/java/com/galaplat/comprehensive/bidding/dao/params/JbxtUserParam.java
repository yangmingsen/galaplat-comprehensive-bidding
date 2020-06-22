package com.galaplat.comprehensive.bidding.dao.params;
import com.galaplat.base.core.common.params.Param;
import java.util.Date;



 /**
 * 用户表Param
 * @author esr
 * @date: 2020年06月17日
 */
public class JbxtUserParam extends Param {
    private static final long serialVersionUID = 1L;
    
    /*唯一编码*/
    private String code;
    /*用户名*/
    private String username;
    /*密码*/
    private String password;
    /*是否为admin(0，普通成员；1，管理员)*/
    private String admin;
    /*创建时间*/
    private Date createdTime;
    /*更新时间如果没有默认创建时间，修改数据必须更新*/
    private Date updatedTime;
    /*更新人*/
    private String updator;
    /*创建人*/
    private String creator;
    /*公司唯一编码*/
    private String companyCode;
    /*系统唯一编码*/
    private String sysCode;

    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public String getAdmin() {
        return admin;
    }
    public void setAdmin(String admin) {
        this.admin = admin;
    }

    public Date getCreatedTime() {
        return createdTime;
    }
    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public Date getUpdatedTime() {
        return updatedTime;
    }
    public void setUpdatedTime(Date updatedTime) {
        this.updatedTime = updatedTime;
    }

    public String getUpdator() {
        return updator;
    }
    public void setUpdator(String updator) {
        this.updator = updator;
    }

    public String getCreator() {
        return creator;
    }
    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getCompanyCode() {
        return companyCode;
    }
    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }

    public String getSysCode() {
        return sysCode;
    }
    public void setSysCode(String sysCode) {
        this.sysCode = sysCode;
    }


}