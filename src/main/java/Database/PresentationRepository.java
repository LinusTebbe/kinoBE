package Database;

import Models.Presentation;

public class PresentationRepository extends AbstractRepository<Presentation> {
    public PresentationRepository() {
        super(Presentation.class);
    }
}
