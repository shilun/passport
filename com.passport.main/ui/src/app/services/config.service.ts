import { Injectable } from '@angular/core';
import {AbstractBaseService} from '../common/abstract-base.service';
import {HttpClient} from '@angular/common/http';

@Injectable()
export class ConfigService extends AbstractBaseService {

  constructor(http: HttpClient) {
    super(http,"/config");
  }

}
