import { Component, OnInit } from '@angular/core';
import {TokenUtils} from '../common/token.utils';
import {ActivatedRoute, Router} from '@angular/router';

@Component({
    selector: 'app-layout',
    templateUrl: './layout.component.html',
    styleUrls: ['./layout.component.scss']
})
export class LayoutComponent implements OnInit {

    constructor(protected route: ActivatedRoute,protected router: Router) { }

    ngOnInit() {
      // if(TokenUtils.getToken()==null){
      //   this.router.navigate(["login"]);
      // }
    }

}
