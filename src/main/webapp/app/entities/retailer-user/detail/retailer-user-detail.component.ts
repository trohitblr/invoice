import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IRetailerUser } from '../retailer-user.model';

@Component({
  selector: 'jhi-retailer-user-detail',
  templateUrl: './retailer-user-detail.component.html',
})
export class RetailerUserDetailComponent implements OnInit {
  retailerUser: IRetailerUser | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ retailerUser }) => {
      this.retailerUser = retailerUser;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
