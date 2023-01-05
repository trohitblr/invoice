import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { RetailerUserFormService, RetailerUserFormGroup } from './retailer-user-form.service';
import { IRetailerUser } from '../retailer-user.model';
import { RetailerUserService } from '../service/retailer-user.service';
import { IRetailer } from 'app/entities/retailer/retailer.model';
import { RetailerService } from 'app/entities/retailer/service/retailer.service';
import { UserType } from 'app/entities/enumerations/user-type.model';

@Component({
  selector: 'jhi-retailer-user-update',
  templateUrl: './retailer-user-update.component.html',
})
export class RetailerUserUpdateComponent implements OnInit {
  isSaving = false;
  retailerUser: IRetailerUser | null = null;
  userTypeValues = Object.keys(UserType);

  retailersSharedCollection: IRetailer[] = [];

  editForm: RetailerUserFormGroup = this.retailerUserFormService.createRetailerUserFormGroup();

  constructor(
    protected retailerUserService: RetailerUserService,
    protected retailerUserFormService: RetailerUserFormService,
    protected retailerService: RetailerService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareRetailer = (o1: IRetailer | null, o2: IRetailer | null): boolean => this.retailerService.compareRetailer(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ retailerUser }) => {
      this.retailerUser = retailerUser;
      if (retailerUser) {
        this.updateForm(retailerUser);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const retailerUser = this.retailerUserFormService.getRetailerUser(this.editForm);
    if (retailerUser.retailerUserId !== null) {
      this.subscribeToSaveResponse(this.retailerUserService.update(retailerUser));
    } else {
      this.subscribeToSaveResponse(this.retailerUserService.create(retailerUser));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IRetailerUser>>): void {
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

  protected updateForm(retailerUser: IRetailerUser): void {
    this.retailerUser = retailerUser;
    this.retailerUserFormService.resetForm(this.editForm, retailerUser);

    this.retailersSharedCollection = this.retailerService.addRetailerToCollectionIfMissing<IRetailer>(
      this.retailersSharedCollection,
      retailerUser.retailers
    );
  }

  protected loadRelationshipsOptions(): void {
    this.retailerService
      .query()
      .pipe(map((res: HttpResponse<IRetailer[]>) => res.body ?? []))
      .pipe(
        map((retailers: IRetailer[]) =>
          this.retailerService.addRetailerToCollectionIfMissing<IRetailer>(retailers, this.retailerUser?.retailers)
        )
      )
      .subscribe((retailers: IRetailer[]) => (this.retailersSharedCollection = retailers));
  }
}
