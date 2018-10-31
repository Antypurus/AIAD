package Company;

import Aggregators.Index;

public class Company {

    private String name;
    private String acronym;

    private Company owner = null;
    private boolean hasOwner = false;

    private Company[] subsidiaries;

    private Index index;

}
