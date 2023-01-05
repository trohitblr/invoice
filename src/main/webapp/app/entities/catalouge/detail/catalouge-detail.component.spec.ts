import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { CatalougeDetailComponent } from './catalouge-detail.component';

describe('Catalouge Management Detail Component', () => {
  let comp: CatalougeDetailComponent;
  let fixture: ComponentFixture<CatalougeDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [CatalougeDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ catalouge: { catalougeId: 123 } }) },
        },
      ],
    })
      .overrideTemplate(CatalougeDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(CatalougeDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load catalouge on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.catalouge).toEqual(expect.objectContaining({ catalougeId: 123 }));
    });
  });
});
