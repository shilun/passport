import { Injectable } from '@angular/core';
import {AbstractBaseService} from '../common/abstract-base.service';
import {HttpClient} from '@angular/common/http';

@Injectable()
export class ProxyService extends AbstractBaseService {

  constructor(http: HttpClient) {
    super(http, '/proxy');
  }

}
