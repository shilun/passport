import 'rxjs/add/operator/toPromise';
import 'rxjs/add/operator/shareReplay';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {TokenUtils} from './token.utils';

export abstract class AbstractBaseService {

  protected path: string;

  constructor(protected http: HttpClient, path: string) {
    this.path = "/api"+path;
  }

  /**
   * 查看
   * @param {string} id
   * @returns {Promise<any>}
   */
  public async view(id: string): Promise<any> {
    return await this.doExec('/view', {id: id});
  }

  public async save(entity: any): Promise<any> {
    return await this.doExec('/save', entity);
  }

  /**
   * 查询
   * @param query
   * @returns {Promise<any>}
   */
  public async list(query: any): Promise<any> {
    return await this.doExec('/list', query);
  }

  /**
   * 查询
   * @param query
   * @returns {Promise<any>}
   */
  public async all(): Promise<any> {
    return await this.doExec('/all', null);
  }

  protected doExec(url: string, data: any): Promise<any> {
    return this.http.post(this.buildUrl(url), data, this.buildHeader()).toPromise();
  }

  protected buildHeader(): any {
    return {
      headers: new HttpHeaders().set('token', this.getToken()).set('Content-Type', 'application/json')
    };
  }

  protected getToken(): string {
    let token: string = TokenUtils.getToken();
    if (token == null) {
      token = '';
    }
    return token;
  }

  private buildUrl(url: any): any {
    return this.path + url;
  }

  protected toUrlParams(parmas): string {
    var paramsStr = '?';
    for (var k in parmas) {
      if (parmas[k] == undefined) {
        continue;
      }
      paramsStr = paramsStr + k + '=' + parmas[k] + '&';
    }
    return paramsStr;
  }

}


