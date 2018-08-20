import { Injectable } from '@angular/core';
import {AbstractBaseService} from '../common/abstract-base.service';
import {HttpClient} from '@angular/common/http';

@Injectable()
export class GlobalService extends AbstractBaseService {

  constructor(http: HttpClient) {
    super(http,"/global");
  }
  /**
   * 查询枚举类型
   * @param query
   * @returns {Promise<any>}
   */
  public async list(type: string): Promise<any> {
    return await this.doExec('/type/'+type,{});
  }
}
