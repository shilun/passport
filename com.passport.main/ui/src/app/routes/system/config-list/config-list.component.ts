import {Component, OnInit} from '@angular/core';
import {AbstractController} from '../../../common/abstract.controller';
import {ActivatedRoute, Router} from '@angular/router';
import {ConfigService} from '../../../services/config.service';

@Component({
  selector: 'app-config-list',
  templateUrl: './config-list.component.html',
  styleUrls: ['./config-list.component.scss']
})
export class ConfigListComponent extends AbstractController implements OnInit {

  constructor(protected configService: ConfigService, protected route: ActivatedRoute, protected router: Router) {
    super(configService, route, router);
    this.entity={page:0,size:10,name:""};
  }


  ngOnInit() {
    this.list(this.entity);
  }

}
