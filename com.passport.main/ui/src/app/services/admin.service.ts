import { Injectable } from '@angular/core';
import {AbstractBaseService} from '../common/abstract-base.service';
import {HttpClient} from '@angular/common/http';

@Injectable()
export class AdminService extends AbstractBaseService {

  constructor(http: HttpClient) {
    super(http, '/admin');
  }

  public async changePass(id: string,password:string): Promise<any> {
    return await this.doExec('/changePass', {id: id,password:password});
  }
}
