import { Pipe, PipeTransform } from '@angular/core';
import {ComplaintStatusEnum} from '../modules/complaint-status-enum.enum';

@Pipe({
  name: 'complaintStatu'
})
export class ComplaintStatuPipe implements PipeTransform {

  transform(value: any, args?: any): any {
    return ComplaintStatusEnum[value];
  }

}
