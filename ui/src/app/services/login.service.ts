import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {AbstractBaseService} from '../common/abstract-base.service';

@Injectable()
export class LoginService extends AbstractBaseService {

  constructor(http: HttpClient) {
    super(http, '/api');
  }

  /**
   * 检查是否登录
   * @returns {Promise<any>}
   */
  public check(): Promise<any> {
    return this.doExec('/login/check', '');
  }


  /**
   * 登录
   * @param {string} name
   * @param {string} pass
   * @returns {Promise<any>}
   */
  public login(name: string, pass: string): Promise<any> {
    return this.doExec('/login', {loginName: name, password: pass, loginType: 1});
  }

  public view(pin: string): Promise<any> {
    return this.doExec('/user/view', {pin: pin});
  }

  /**
   * 登出
   * @returns {Promise<any>}
   */
  public loginOut(): Promise<any> {
    return this.doExec('/login/out', '');
  }

}
