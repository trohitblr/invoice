import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IRetailInventory } from '../retail-inventory.model';

@Component({
  selector: 'jhi-retail-inventory-detail',
  templateUrl: './retail-inventory-detail.component.html',
})
export class RetailInventoryDetailComponent implements OnInit {
  retailInventory: IRetailInventory | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ retailInventory }) => {
      this.retailInventory = retailInventory;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
