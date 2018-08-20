import { Pipe, PipeTransform } from '@angular/core';
import {PayStatusEnum} from '../modules/pay-status-enum.enum';

@Pipe({
  name: 'payStatus'
})
export class PayStatusPipe implements PipeTransform {

  transform(value: any, args?: any): any {
    return PayStatusEnum[value];
  }

}
