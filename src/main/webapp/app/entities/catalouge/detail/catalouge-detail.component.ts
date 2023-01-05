import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICatalouge } from '../catalouge.model';

@Component({
  selector: 'jhi-catalouge-detail',
  templateUrl: './catalouge-detail.component.html',
})
export class CatalougeDetailComponent implements OnInit {
  catalouge: ICatalouge | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ catalouge }) => {
      this.catalouge = catalouge;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
