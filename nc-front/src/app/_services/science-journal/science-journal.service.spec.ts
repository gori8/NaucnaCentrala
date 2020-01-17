import { TestBed } from '@angular/core/testing';

import { ScienceJournalService } from './science-journal.service';

describe('ScienceJournalService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: ScienceJournalService = TestBed.get(ScienceJournalService);
    expect(service).toBeTruthy();
  });
});
