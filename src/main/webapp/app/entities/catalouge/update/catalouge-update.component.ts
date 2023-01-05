import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { CatalougeFormService, CatalougeFormGroup } from './catalouge-form.service';
import { ICatalouge } from '../catalouge.model';
import { CatalougeService } from '../service/catalouge.service';
import { ICategories } from 'app/entities/categories/categories.model';
import { CategoriesService } from 'app/entities/categories/service/categories.service';
import { IGstSlave } from 'app/entities/gst-slave/gst-slave.model';
import { GstSlaveService } from 'app/entities/gst-slave/service/gst-slave.service';

@Component({
  selector: 'jhi-catalouge-update',
  templateUrl: './catalouge-update.component.html',
})
export class CatalougeUpdateComponent implements OnInit {
  isSaving = false;
  catalouge: ICatalouge | null = null;

  categoriesSharedCollection: ICategories[] = [];
  gstSlavesSharedCollection: IGstSlave[] = [];

  editForm: CatalougeFormGroup = this.catalougeFormService.createCatalougeFormGroup();

  constructor(
    protected catalougeService: CatalougeService,
    protected catalougeFormService: CatalougeFormService,
    protected categoriesService: CategoriesService,
    protected gstSlaveService: GstSlaveService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareCategories = (o1: ICategories | null, o2: ICategories | null): boolean => this.categoriesService.compareCategories(o1, o2);

  compareGstSlave = (o1: IGstSlave | null, o2: IGstSlave | null): boolean => this.gstSlaveService.compareGstSlave(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ catalouge }) => {
      this.catalouge = catalouge;
      if (catalouge) {
        this.updateForm(catalouge);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const catalouge = this.catalougeFormService.getCatalouge(this.editForm);
    if (catalouge.catalougeId !== null) {
      this.subscribeToSaveResponse(this.catalougeService.update(catalouge));
    } else {
      this.subscribeToSaveResponse(this.catalougeService.create(catalouge));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICatalouge>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(catalouge: ICatalouge): void {
    this.catalouge = catalouge;
    this.catalougeFormService.resetForm(this.editForm, catalouge);

    this.categoriesSharedCollection = this.categoriesService.addCategoriesToCollectionIfMissing<ICategories>(
      this.categoriesSharedCollection,
      catalouge.categorie
    );
    this.gstSlavesSharedCollection = this.gstSlaveService.addGstSlaveToCollectionIfMissing<IGstSlave>(
      this.gstSlavesSharedCollection,
      catalouge.taxSlaves
    );
  }

  protected loadRelationshipsOptions(): void {
    this.categoriesService
      .query()
      .pipe(map((res: HttpResponse<ICategories[]>) => res.body ?? []))
      .pipe(
        map((categories: ICategories[]) =>
          this.categoriesService.addCategoriesToCollectionIfMissing<ICategories>(categories, this.catalouge?.categorie)
        )
      )
      .subscribe((categories: ICategories[]) => (this.categoriesSharedCollection = categories));

    this.gstSlaveService
      .query()
      .pipe(map((res: HttpResponse<IGstSlave[]>) => res.body ?? []))
      .pipe(
        map((gstSlaves: IGstSlave[]) =>
          this.gstSlaveService.addGstSlaveToCollectionIfMissing<IGstSlave>(gstSlaves, this.catalouge?.taxSlaves)
        )
      )
      .subscribe((gstSlaves: IGstSlave[]) => (this.gstSlavesSharedCollection = gstSlaves));
  }
}
