import { Pipe, PipeTransform } from '@angular/core';
import {OrderStatusEnum} from '../modules/order-status-enum.enum';

@Pipe({
  name: 'orderStatus'
})
export class OrderStatusPipe implements PipeTransform {

  transform(value: any, args?: any): any {
    return OrderStatusEnum[value];
  }

}
