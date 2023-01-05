import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { RetailInventoryDetailComponent } from './retail-inventory-detail.component';

describe('RetailInventory Management Detail Component', () => {
  let comp: RetailInventoryDetailComponent;
  let fixture: ComponentFixture<RetailInventoryDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [RetailInventoryDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ retailInventory: { retailInventoryId: 123 } }) },
        },
      ],
    })
      .overrideTemplate(RetailInventoryDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(RetailInventoryDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load retailInventory on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.retailInventory).toEqual(expect.objectContaining({ retailInventoryId: 123 }));
    });
  });
});
