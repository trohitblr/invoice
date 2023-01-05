import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { MediaService } from '../service/media.service';

import { MediaComponent } from './media.component';

describe('Media Management Component', () => {
  let comp: MediaComponent;
  let fixture: ComponentFixture<MediaComponent>;
  let service: MediaService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [RouterTestingModule.withRoutes([{ path: 'media', component: MediaComponent }]), HttpClientTestingModule],
      declarations: [MediaComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: {
            data: of({
              defaultSort: 'mediaId,asc',
            }),
            queryParamMap: of(
              jest.requireActual('@angular/router').convertToParamMap({
                page: '1',
                size: '1',
                sort: 'mediaId,desc',
              })
            ),
            snapshot: { queryParams: {} },
          },
        },
      ],
    })
      .overrideTemplate(MediaComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(MediaComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(MediaService);

    const headers = new HttpHeaders();
    jest.spyOn(service, 'query').mockReturnValue(
      of(
        new HttpResponse({
          body: [{ mediaId: 123 }],
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
    expect(comp.media?.[0]).toEqual(expect.objectContaining({ mediaId: 123 }));
  });

  describe('trackMediaId', () => {
    it('Should forward to mediaService', () => {
      const entity = { mediaId: 123 };
      jest.spyOn(service, 'getMediaIdentifier');
      const mediaId = comp.trackMediaId(0, entity);
      expect(service.getMediaIdentifier).toHaveBeenCalledWith(entity);
      expect(mediaId).toBe(entity.mediaId);
    });
  });
});
