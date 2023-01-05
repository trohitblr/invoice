import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { RetailerUserDetailComponent } from './retailer-user-detail.component';

describe('RetailerUser Management Detail Component', () => {
  let comp: RetailerUserDetailComponent;
  let fixture: ComponentFixture<RetailerUserDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [RetailerUserDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ retailerUser: { retailerUserId: 123 } }) },
        },
      ],
    })
      .overrideTemplate(RetailerUserDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(RetailerUserDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load retailerUser on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.retailerUser).toEqual(expect.objectContaining({ retailerUserId: 123 }));
    });
  });
});
