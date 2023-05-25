import { Component, ElementRef, Inject, OnDestroy, OnInit, ViewChild } from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import { Reason } from '../view-received-requests/view-received-requests.component';


@Component({
  selector: 'app-input-reason',
  templateUrl: './input-reason.component.html',
  styleUrls: ['./input-reason.component.css']
})
export class InputReasonComponent implements OnInit {


  @ViewChild('rejection') rejection!: ElementRef;

  constructor(@Inject(MAT_DIALOG_DATA) public data: Reason, public matDialogRef: MatDialogRef<InputReasonComponent>) { }

  ngOnInit(): void {
  }

  ngOnDestroy(): void {
    this.matDialogRef.close(this.data);
  }

  onCloseDialog() {
    this.matDialogRef.close();
  }

  onConfirm() {
    const textarea = this.rejection.nativeElement;
    this.data.reason = textarea.value;
    this.ngOnDestroy();
  }

}
