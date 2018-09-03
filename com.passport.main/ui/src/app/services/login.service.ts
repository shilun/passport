import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {AbstractBaseService} from '../common/abstract-base.service';

@Injectable()
export class LoginService extends AbstractBaseService {

  constructor(http: HttpClient) {
    super(http, '/login');
  }

  /**
   * 检查是否登录
   * @returns {Promise<any>}
   */
  public check(): Promise<any> {
    return this.doExec('/check', '');
  }


  /**
   * 登录
   * @param {string} name
   * @param {string} pass
   * @returns {Promise<any>}
   */
  public login(name: string, pass: string): Promise<any> {
    return this.doExec('/in', {account: name, code: pass});
  }

  /**
   * 登出
   * @returns {Promise<any>}
   */
  public loginOut(): Promise<any> {
    return this.doExec('/out', '');
  }

  /**
   * 修改密码
   * @returns {Promise<any>}
   */
  public changePass(oldPass:string,newPass): Promise<any> {
    return this.doExec('/changePass', {oldPassword:oldPass,newPassword:newPass});
  }

}
