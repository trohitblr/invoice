import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'retailer',
        data: { pageTitle: 'Retailers' },
        loadChildren: () => import('./retailer/retailer.module').then(m => m.RetailerModule),
      },
      {
        path: 'address',
        data: { pageTitle: 'Addresses' },
        loadChildren: () => import('./address/address.module').then(m => m.AddressModule),
      },
      {
        path: 'media',
        data: { pageTitle: 'Media' },
        loadChildren: () => import('./media/media.module').then(m => m.MediaModule),
      },
      {
        path: 'customer',
        data: { pageTitle: 'Customers' },
        loadChildren: () => import('./customer/customer.module').then(m => m.CustomerModule),
      },
      {
        path: 'invoice',
        data: { pageTitle: 'Invoices' },
        loadChildren: () => import('./invoice/invoice.module').then(m => m.InvoiceModule),
      },
      {
        path: 'retailer-user',
        data: { pageTitle: 'RetailerUsers' },
        loadChildren: () => import('./retailer-user/retailer-user.module').then(m => m.RetailerUserModule),
      },
      {
        path: 'invoice-line-item',
        data: { pageTitle: 'InvoiceLineItems' },
        loadChildren: () => import('./invoice-line-item/invoice-line-item.module').then(m => m.InvoiceLineItemModule),
      },
      {
        path: 'invoice-g-st-line-item',
        data: { pageTitle: 'InvoiceGStLineItems' },
        loadChildren: () => import('./invoice-g-st-line-item/invoice-g-st-line-item.module').then(m => m.InvoiceGStLineItemModule),
      },
      {
        path: 'catalouge',
        data: { pageTitle: 'Catalouges' },
        loadChildren: () => import('./catalouge/catalouge.module').then(m => m.CatalougeModule),
      },
      {
        path: 'categories',
        data: { pageTitle: 'Categories' },
        loadChildren: () => import('./categories/categories.module').then(m => m.CategoriesModule),
      },
      {
        path: 'gst-slave',
        data: { pageTitle: 'GstSlaves' },
        loadChildren: () => import('./gst-slave/gst-slave.module').then(m => m.GstSlaveModule),
      },
      {
        path: 'retail-inventory',
        data: { pageTitle: 'RetailInventories' },
        loadChildren: () => import('./retail-inventory/retail-inventory.module').then(m => m.RetailInventoryModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
