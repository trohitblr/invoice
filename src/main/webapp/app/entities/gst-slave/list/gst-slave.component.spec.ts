import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { GstSlaveService } from '../service/gst-slave.service';

import { GstSlaveComponent } from './gst-slave.component';

describe('GstSlave Management Component', () => {
  let comp: GstSlaveComponent;
  let fixture: ComponentFixture<GstSlaveComponent>;
  let service: GstSlaveService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [RouterTestingModule.withRoutes([{ path: 'gst-slave', component: GstSlaveComponent }]), HttpClientTestingModule],
      declarations: [GstSlaveComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: {
            data: of({
              defaultSort: 'gstSlaveId,asc',
            }),
            queryParamMap: of(
              jest.requireActual('@angular/router').convertToParamMap({
                page: '1',
                size: '1',
                sort: 'gstSlaveId,desc',
              })
            ),
            snapshot: { queryParams: {} },
          },
        },
      ],
    })
      .overrideTemplate(GstSlaveComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(GstSlaveComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(GstSlaveService);

    const headers = new HttpHeaders();
    jest.spyOn(service, 'query').mockReturnValue(
      of(
        new HttpResponse({
          body: [{ gstSlaveId: 123 }],
          headers,
        })
      )
    );
  });

  it('Should call load all on init', () => {
    // WHEN
    comp.ngOnInit();

    // THEN
    expect(service.query).toHaveBeenCalled();
    expect(comp.gstSlaves?.[0]).toEqual(expect.objectContaining({ gstSlaveId: 123 }));
  });

  describe('trackGstSlaveId', () => {
    it('Should forward to gstSlaveService', () => {
      const entity = { gstSlaveId: 123 };
      jest.spyOn(service, 'getGstSlaveIdentifier');
      const gstSlaveId = comp.trackGstSlaveId(0, entity);
      expect(service.getGstSlaveIdentifier).toHaveBeenCalledWith(entity);
      expect(gstSlaveId).toBe(entity.gstSlaveId);
    });
  });
});
