import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { GstSlaveDetailComponent } from './gst-slave-detail.component';

describe('GstSlave Management Detail Component', () => {
  let comp: GstSlaveDetailComponent;
  let fixture: ComponentFixture<GstSlaveDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [GstSlaveDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ gstSlave: { gstSlaveId: 123 } }) },
        },
      ],
    })
      .overrideTemplate(GstSlaveDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(GstSlaveDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load gstSlave on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.gstSlave).toEqual(expect.objectContaining({ gstSlaveId: 123 }));
    });
  });
});
