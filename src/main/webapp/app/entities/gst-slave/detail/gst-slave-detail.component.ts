import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IGstSlave } from '../gst-slave.model';

@Component({
  selector: 'jhi-gst-slave-detail',
  templateUrl: './gst-slave-detail.component.html',
})
export class GstSlaveDetailComponent implements OnInit {
  gstSlave: IGstSlave | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ gstSlave }) => {
      this.gstSlave = gstSlave;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
